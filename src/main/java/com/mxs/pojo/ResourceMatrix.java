package com.mxs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * @Description: 资源矩阵，用于银行家算法的计算，不用于数据库存储
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ResourceMatrix {
    private Integer n; //进程数
    private Integer m; //资源种类数
    private Integer[] available;//可用资源
    private Integer[][] max;//最大需求资源
    private Integer[][] allocation;//已分配资源
    private Integer[][] need;// 需求资源
    private Integer[] executeTime;//每个进程的执行时间

    private Integer requestProcess;//是哪个进程请求资源
    private Integer[] request;//该进程请求的资源

    /*
     * @Description: 构造函数，初始化资源矩阵
     */
    public ResourceMatrix(Integer n, Integer m, Integer requestProcess) {
        Random r = new Random();
        //随机初始化资源数与进程数
        this.n = n;
        this.m = m;
        this.requestProcess = requestProcess;
        //初始化数组

        available = new Integer[m];
        max = new Integer[n][m];
        allocation = new Integer[n][m];
        need = new Integer[n][m];
        request = new Integer[m];
        executeTime = new Integer[n];
        //可用资源数随机赋初值
        for (int j = 0; j < m; j++) {
            available[j] = r.nextInt(40, 100);
            //随机化请求
            request[j] = r.nextInt(0, 10);
        }
        //随即赋初值
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                //随机化最大需求
                max[i][j] = r.nextInt(20, 40);
                //已分配资源不能超过最大需求资源
                allocation[i][j] = r.nextInt(0, max[i][j] - 10);
                //计算需求资源
                need[i][j] = max[i][j] - allocation[i][j];
            }
            //随机化每个进程的执行时间
            executeTime[i] = r.nextInt(0, 100);
        }
    }

    /*
     * @Description: 银行家算法
     */
    public List<List<Integer>> dispatch() {
        //试分配资源
        boolean canAllocate = allocationTrial();
        if (!canAllocate) {
            return new ArrayList<>();
        }
        //查找所有安全序列(安全算法)
        List<List<Integer>> safeList = findAllSafeSequences();
        log.info("safeList:{}", safeList);
        //根据资源利用效率排序
        RankListByEfficiency(safeList);
        return safeList;
    }

    /*
     * @Description: 银行家算法第一步，比较，试分配资源
     */
    private boolean allocationTrial() {
        boolean canAllocate = true;
        //请求资源不大于可用资源
        for (int j = 0; j < m; j++) {
            if (request[j] > available[j]) {
                canAllocate = false;
                break;
            }
        }
        //请求资源不大于需要的资源
        for (int j = 0; j < m; j++) {
            if (request[j] > need[requestProcess][j]) {
                canAllocate = false;
                break;
            }
        }
        // 假设分配资源
        if (canAllocate) {
            for (int j = 0; j < m; j++) {
                available[j] -= request[j];
                allocation[requestProcess][j] += request[j];
                need[requestProcess][j] -= request[j];
            }
        }
        return canAllocate;
    }

    /*
     * @Description: 根据资源利用效率排序
     */
    private void RankListByEfficiency(List<List<Integer>> safeList) {
        // 创建一个列表来存储每个安全序列及其效率
        List<SequenceEfficiency> sequenceEfficiencies = new ArrayList<>();
        for (List<Integer> sequence : safeList) {
            // 计算每个安全序列的资源利用效率
            double efficiency = calculateEfficiency(sequence);
            sequenceEfficiencies.add(new SequenceEfficiency(sequence, efficiency));
        }
        // 根据效率对安全序列进行排序
        sequenceEfficiencies.sort((a, b) -> Double.compare(b.efficiency, a.efficiency));
        // 清空原始安全序列列表并按效率排序重新添加
        safeList.clear();
        for (SequenceEfficiency se : sequenceEfficiencies) {
            safeList.add(se.sequence);
        }
    }

    /*
     *  @Description: 计算效率
     */
    private double calculateEfficiency(List<Integer> sequence) {
        // 计算每个资源的总量
        Integer[] total = new Integer[m];
        for (int j = 0; j < m; j++) {
            total[j] = available[j]; // 试分配后的available
            for (int i = 0; i < n; i++) {
                total[j] += allocation[i][j];
            }
        }

        // 初始化work为试分配后的available的拷贝
        Integer[] work = Arrays.copyOf(available, m);

        double totalUtilization = 0.0;
        int totalTime = 0;

        // 计算总时间（所有进程的执行时间之和）
        for (int process : sequence) {
            totalTime += executeTime[process];
        }

        if (totalTime == 0) {
            return 0.0; // 避免除零错误
        }

        // 遍历安全序列中的每个进程
        for (int process : sequence) {
            // 计算当前进程执行时的资源利用率
            double processUtilization = 0.0;
            for (int j = 0; j < m; j++) {
                if (total[j] == 0) {
                    continue; // 跳过无效资源
                }
                // 资源利用率为 (总资源 - 当前可用资源) / 总资源
                double utilization = (double) (total[j] - work[j]) / total[j];
                processUtilization += utilization;
            }
            // 平均各资源类型的利用率
            processUtilization /= m;

            // 累加时间加权的利用率
            totalUtilization += processUtilization * executeTime[process];

            // 更新可用资源：进程执行完成，释放资源
            for (int j = 0; j < m; j++) {
                work[j] += allocation[process][j];
            }
        }

        double ans = totalUtilization / totalTime;

        log.info("序列：{},效率{}",sequence,ans);

        // 计算整体平均效率
        return ans;
    }

    /*
     *  @Description:静态类sequenceEfficiency用于对安全序列排序
     */
    private static class SequenceEfficiency {
        List<Integer> sequence;
        double efficiency;

        SequenceEfficiency(List<Integer> sequence, double efficiency) {
            this.sequence = sequence;
            this.efficiency = efficiency;
        }
    }

    /*/
     * @Description: 查找所有安全序列
     */
    private List<List<Integer>> findAllSafeSequences() {
        List<List<Integer>> allSafeSequences = new ArrayList<>();//所有安全序列
        List<Integer> currentSequence = new ArrayList<>();//当前序列
        boolean[] finish = new boolean[n];//回溯标记
        findSafeSequencesHelper(allSafeSequences, currentSequence, available, finish);
        return allSafeSequences;
    }


    private void findSafeSequencesHelper(List<List<Integer>> allSafeSequences, List<Integer> currentSequence, Integer[] work, boolean[] finish) {
        if (currentSequence.size() == n) { //递归完毕
            allSafeSequences.add(new ArrayList<>(currentSequence));
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!finish[i] && canAllocate(i, work)) {
                Integer[] newWork = allocateResource(i, work);
                finish[i] = true;
                currentSequence.add(i);
                //递归
                findSafeSequencesHelper(allSafeSequences, currentSequence, newWork, finish);
                //回溯
                currentSequence.remove(currentSequence.size() - 1);
                finish[i] = false;
            }

        }
    }

    /*
        返回新的work向量为work+allocation
     */
    private Integer[] allocateResource(int i, Integer[] work) {
        Integer[] newWork = Arrays.copyOf(work, work.length);
        for (int j = 0; j < m; j++) {
            newWork[j] += allocation[i][j];
        }
        return newWork;
    }

    /*
        检查need是否大于available
     */
    private boolean canAllocate(int i, Integer[] work) {
        boolean canAllocate = true;
        for (int j = 0; j < m; j++) {
            if (need[i][j] > work[j]) {
                canAllocate = false;
                break;
            }
        }
        return canAllocate;
    }


    /*
     * @Description: 判断当前资源矩阵是否安全，好像用不到了
     */
    @Deprecated
    public boolean isSafe() {
        // 初始化工作向量 work 和 finish 向量
        Integer[] work = new Integer[m];
        boolean[] finish = new boolean[n];
        for (int i = 0; i < m; i++) {
            work[i] = available[i];
        }
        for (int i = 0; i < n; i++) {
            finish[i] = false;
        }

        // 查找一个进程，使得 finish[i] 为 false 且 need[i] <= work
        boolean foundProcess;
        do {
            foundProcess = false;
            for (int i = 0; i < n; i++) {
                if (!finish[i]) {
                    boolean canAllocate = true;
                    for (int j = 0; j < m; j++) {
                        if (need[i][j] > work[j]) {
                            canAllocate = false;
                            break;
                        }
                    }
                    if (canAllocate) {
                        // 如果找到了这样的进程，假设它可以执行完毕
                        for (int j = 0; j < m; j++) {
                            work[j] += allocation[i][j];
                        }
                        finish[i] = true;
                        foundProcess = true;
                    }
                }
            }
        } while (foundProcess);

        // 检查所有进程是否都能执行完毕
        for (int i = 0; i < n; i++) {
            if (!finish[i]) {
                return false;
            }
        }
        return true;
    }

    /*
     * @Description: 资源矩阵转换为资源表，可以保存在数据库中
     */
    public ResourceTable toResourceTable() {
        ResourceTable resourceTable = new ResourceTable(null, n, m,
                Arrays.toString(available), Arrays.deepToString(max),
                Arrays.deepToString(allocation), Arrays.deepToString(need),
                Arrays.toString(executeTime), requestProcess, Arrays.toString(request), LocalDateTime.now());
        return resourceTable;
    }
}
