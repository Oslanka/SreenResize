package xweb.oslanka.com.myapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by zhy on 15/5/3.
 */
public class GenerateValueFiles {

    private int baseW;
    private int baseH;

    private String dirStr = "app/src/res";

    private float baseDpi = 2.0f;
    private float dpi = 2.0f;
    private final static String WTemplate = "<dimen name=\"dp_{0}\">{1}dp</dimen>\n";
//	private final static String HTemplate = "<dimen name=\"y{0}\">{1}dp</dimen>\n";

    /**
     * {0}-HEIGHT
     */
    private static String VALUE_TEMPLATE = "values-xhdpi-{0}x{1}";

    private static final String SUPPORT_DIMESION = "1080,1920;1536,2048;1200,1920;800,1280;";

    private String supportStr = SUPPORT_DIMESION;

    public GenerateValueFiles(int baseX, int baseY, String supportStr) {
        this.baseW = baseX;
        this.baseH = baseY;

        if (!this.supportStr.contains(baseX + "," + baseY)) {
            this.supportStr += baseX + "," + baseY + ";";
        }

        this.supportStr += validateInput(supportStr);

        System.out.println(supportStr);

        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdirs();

        }
        System.out.println(dir.getAbsoluteFile());

    }

    /**
     * @param supportStr w,h_...w,h;
     * @return
     */
    private String validateInput(String supportStr) {
        StringBuffer sb = new StringBuffer();
        String[] vals = supportStr.split("_");
        int w = -1;
        int h = -1;
        String[] wh;
        for (String val : vals) {
            try {
                if (val == null || val.trim().length() == 0)
                    continue;

                wh = val.split(",");
                w = Integer.parseInt(wh[0]);
                h = Integer.parseInt(wh[1]);
            } catch (Exception e) {
                System.out.println("skip invalidate params : w,h = " + val);
                continue;
            }
            sb.append(w + "," + h + ";");
        }

        return sb.toString();
    }

    public void generate() {
        String[] vals = supportStr.split(";");
        for (int i = 0; i < 4; i++) {
            //
            if (i == 0) {
                dpi = 2.0f;
                VALUE_TEMPLATE = "values-xhdpi-{0}x{1}";
            } else if (i == 1) {
                dpi = 1.5f;
                VALUE_TEMPLATE = "values-hdpi-{0}x{1}";
            } else if (i == 2) {
                dpi = 1.0f;
                VALUE_TEMPLATE = "values-mdpi-{0}x{1}";
            } else if (i == 3) {
                dpi = 3.0f;
                VALUE_TEMPLATE = "values-xxhdpi-{0}x{1}";
            }
            for (String val : vals) {
                String[] wh = val.split(",");
                generateXmlFile(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
            }

        }


    }

    private void generateXmlFile(int w, int h) {

        StringBuffer sbForWidth = new StringBuffer();
        sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForWidth.append("<resources>");
//        float cellw = baseDpi / dpi * ((1.0f * w / baseW + 1.0f * h / baseH) / 2);
        float cellw = baseDpi / dpi * ((1.0f * h / baseH ) );

//		System.out.println("width : " + w + "," + baseW + "," + cellw);
        for (int i = 1; i < 500; i++) {
            sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }
//        sbForWidth.append(WTemplate.replace("{0}", baseW + "").replace("{1}",
//                w + ""));
        sbForWidth.append("</resources>");

//		StringBuffer sbForHeight = new StringBuffer();
//		sbForHeight.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
//		sbForHeight.append("<resources>");
//		float cellh = h *1.0f/ baseH;
//		System.out.println("height : "+ h + "," + baseH + "," + cellh);
//		for (int i = 1; i < baseH; i++) {
//			sbForHeight.append(HTemplate.replace("{0}", i + "").replace("{1}",
//					change(cellh * i) + ""));
//		}
//		sbForHeight.append(HTemplate.replace("{0}", baseH + "").replace("{1}",
//				h + ""));
//		sbForHeight.append("</resources>");

        File fileDir = new File(dirStr + File.separator
                + VALUE_TEMPLATE.replace("{0}", h + "")//
                .replace("{1}", w + ""));
        fileDir.mkdirs();

        File layxFile = new File(fileDir.getAbsolutePath(), "dimens.xml");
        System.out.print("\n" + layxFile.getAbsoluteFile());
//		File layyFile = new File(fileDir.getAbsolutePath(), "lay_y.xml");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(layxFile));
            pw.print(sbForWidth.toString());
            pw.close();
//			pw = new PrintWriter(new FileOutputStream(layyFile));
//			pw.print(sbForHeight.toString());
//			pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    public static void main(String[] args) {
        int baseW = 1080;
        int baseH = 1920;
        String addition = "";
        try {
            if (args.length >= 3) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
                addition = args[2];
            } else if (args.length >= 2) {
                baseW = Integer.parseInt(args[0]);
                baseH = Integer.parseInt(args[1]);
            } else if (args.length >= 1) {
                addition = args[0];
            }
        } catch (NumberFormatException e) {

            System.err
                    .println("right input params : java -jar xxx.jar width height w,h_w,h_..._w,h;");
            e.printStackTrace();
            System.exit(-1);
        }

        new GenerateValueFiles(baseW, baseH, addition).generate();
    }

}