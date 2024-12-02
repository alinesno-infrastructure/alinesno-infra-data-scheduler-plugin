package com.alinesno.infra.data.scheduler.kubernetes.checker;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SecurityComplianceChecker {

    public static void main(String[] args) {
        checkSystemUpdates();
        checkFirewallStatus();
        checkSSHConfig();
        // 可以添加更多检查点...
    }

    private static void checkSystemUpdates() {
        try {
            Process process = Runtime.getRuntime().exec("apt-get -s upgrade | grep 'upgraded, '");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("upgraded, ")) {
                    System.out.println("[WARNING] System updates are available.");
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkFirewallStatus() {
        try {
            Process process = Runtime.getRuntime().exec("sudo ufw status");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean isActive = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Status: active")) {
                    isActive = true;
                    break;
                }
            }
            if (!isActive) {
                System.out.println("[WARNING] Firewall is not active.");
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkSSHConfig() {
        try {
            Process process = Runtime.getRuntime().exec("grep -i '^permitrootlogin' /etc/ssh/sshd_config");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("yes")) {
                    System.out.println("[WARNING] Root login via SSH is allowed.");
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}