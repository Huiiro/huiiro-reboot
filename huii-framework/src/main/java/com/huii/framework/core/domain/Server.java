package com.huii.framework.core.domain;

import com.huii.common.utils.request.IpAddressUtils;
import com.huii.oss.utils.FileUtils;
import lombok.Data;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Data
public class Server {
    private static final int WAIT_SECOND = 1000;

    private Cpu cpu = new Cpu();

    private Mem mem = new Mem();

    private Jvm jvm = new Jvm();

    private Sys sys = new Sys();

    private List<File> files = new LinkedList<>();

    public void getServerInfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        setCpuInfo(hardware.getProcessor());
        setMemInfo(hardware.getMemory());
        setFileInfo(systemInfo.getOperatingSystem());
        setSysInfo();
        setJvmInfo();
    }

    /**
     * 获取cpu信息
     */
    private void setCpuInfo(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpu.setCpuCore(processor.getLogicalProcessorCount());
        cpu.setCpuType(processor.getProcessorIdentifier().getName());
        cpu.setTotalUsed(Double.parseDouble(String.format("%.2f", (double) totalCpu / 1000)));
        cpu.setSysUsed(Double.parseDouble(String.format("%.2f", (double) sys / 1000)));
        cpu.setUserUsed(Double.parseDouble(String.format("%.2f", (double) user / 1000)));
        cpu.setWait(Double.parseDouble(String.format("%.2f", (double) iowait / 1000)));
        cpu.setFree(Double.parseDouble(String.format("%.2f", (double) idle / 1000)));
    }

    /**
     * 获取mem信息
     */
    private void setMemInfo(GlobalMemory memory) {
        long bytesToGB = 1024 * 1024 * 1024;
        mem.setTotal(Double.parseDouble(String.format("%.2f", (double) memory.getTotal() / bytesToGB)));
        mem.setUsed(Double.parseDouble(String.format("%.2f", (double) (memory.getTotal() - memory.getAvailable()) / bytesToGB)));
        mem.setFree(Double.parseDouble(String.format("%.2f", (double) memory.getAvailable() / bytesToGB)));
    }

    /**
     * 获取磁盘信息
     */
    private void setFileInfo(OperatingSystem operatingSystem) {
        FileSystem fileSystem = operatingSystem.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            File file = new File();
            file.setDirName(fs.getMount());
            file.setSysTypeName(fs.getType());
            file.setTypeName(fs.getName());
            file.setTotal(FileUtils.formatFileSize(total));
            file.setFree(FileUtils.formatFileSize(free));
            file.setUsed(FileUtils.formatFileSize(used));
            file.setUsage(calcUsage(used, total));
            files.add(file);
        }
    }

    /**
     * 获取系统信息
     */
    private void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(IpAddressUtils.getHostName());
        sys.setComputerIp(IpAddressUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 获取jvm信息
     */
    private void setJvmInfo() {
        Properties props = System.getProperties();
        long bytesToMB = 1024 * 1024;
        jvm.setTotal(Double.parseDouble(String.format("%.2f", (double) Runtime.getRuntime().totalMemory() / bytesToMB)));
        jvm.setMax(Double.parseDouble(String.format("%.2f", (double) Runtime.getRuntime().maxMemory() / bytesToMB)));
        jvm.setFree(Double.parseDouble(String.format("%.2f", (double) Runtime.getRuntime().freeMemory() / bytesToMB)));
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    private double calcUsage(long v1, long v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        double vt = b1.divide(b2, 4, RoundingMode.HALF_UP).doubleValue();
        BigDecimal b3 = new BigDecimal(Double.toString(vt));
        BigDecimal b4 = new BigDecimal(Double.toString((long) 100));
        return b4.multiply(b3).doubleValue();
    }
}
