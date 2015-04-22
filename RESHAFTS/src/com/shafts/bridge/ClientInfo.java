package com.shafts.bridge;

/**
 *
 * @author Little-Kitty company CHINA ECUST.
 * @version V1.0 date: 2014年9月5日 上午8:48:51
 */
public class ClientInfo {

    private String src1;
    private String src2;
    private String dst;

    /**
     * generate dst
     */
    @SuppressWarnings("static-access")
    private void generate() {
        gwm = new GetWindowsMACAddress();
        src1 = gwm.getAddress().replace("-", "");
        src2 = new GetSerial().getCPUSerial();
        String part1 = src2.substring(0, 2) + src2.substring(src2.length() - 2, src2.length());
        String part2 = src2.substring(2, 4) + src2.substring(src2.length() - 4, src2.length() - 2);
        StringBuilder sb = new StringBuilder(src1);
        sb.insert(4, part1);
        sb.insert(src1.length(), part2);
        dst = sb.toString();
    }

    public String getDst() {
        generate();
        return dst;
    }

    public static void main(String args[]) {
        String a = new ClientInfo().getDst();
        System.out.println(a);
    }
    private GetWindowsMACAddress gwm;
}
