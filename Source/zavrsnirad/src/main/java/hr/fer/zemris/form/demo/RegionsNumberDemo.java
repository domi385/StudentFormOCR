package hr.fer.zemris.form.demo;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class calculates segmentation result in development.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 2.6.2017.
 */
public class RegionsNumberDemo {

    /**
     * Visitor for counting segments.
     *
     * @author Domagoj Pluscec
     * @version v1.0, 2.6.2017.
     */
    private static class CounterVisitor extends SimpleFileVisitor<Path> {

        private Map<String, Map<String, Integer>> count;

        CounterVisitor() {
            count = new HashMap<String, Map<String, Integer>>();
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (!dir.toAbsolutePath().toString().contains("field")) {
                return FileVisitResult.CONTINUE;

            }
            String dirName = dir.getFileName().toString();
            if (dirName.contains("field")) {
                count.put(dirName, new HashMap<String, Integer>());
                return FileVisitResult.CONTINUE;
            }
            String parentName = dir.getParent().getFileName().toString();
            count.get(parentName).put(dirName, dir.toFile().listFiles().length);
            return FileVisitResult.CONTINUE;
        }
    }

    private static Map<String, Integer> getExpected() {
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("field_000", 1);
        expected.put("field_001", 1);
        expected.put("field_002", 1);
        expected.put("field_003", 1);
        expected.put("field_004", 1);
        expected.put("field_005", 1);
        expected.put("field_006", 1);
        expected.put("field_007", 1);
        expected.put("field_008", 1);
        expected.put("field_009", 1);
        expected.put("field_010", 2);
        expected.put("field_011", 2);
        expected.put("field_012", 2);
        expected.put("field_013", 2);
        expected.put("field_014", 2);
        expected.put("field_015", 2);
        expected.put("field_016", 2);
        expected.put("field_017", 2);
        expected.put("field_018", 2);
        expected.put("field_019", 2);
        expected.put("field_020", 3);
        expected.put("field_021", 3);
        expected.put("field_022", 3);
        expected.put("field_023", 3);
        expected.put("field_024", 3);
        expected.put("field_025", 3);
        expected.put("field_026", 3);
        expected.put("field_027", 3);

        expected.put("field_028", 3);
        expected.put("field_029", 3);
        expected.put("field_030", 5);
        expected.put("field_031", 5);
        expected.put("field_032", 5);
        expected.put("field_033", 5);
        expected.put("field_034", 5);
        expected.put("field_035", 5);
        expected.put("field_036", 5);
        expected.put("field_037", 5);
        expected.put("field_038", 4);
        expected.put("field_039", 5);
        expected.put("field_040", 5);
        expected.put("field_041", 5);
        expected.put("field_042", 6);
        expected.put("field_043", 6);
        expected.put("field_044", 6);
        expected.put("field_045", 6);
        expected.put("field_046", 5);
        expected.put("field_047", 5);
        expected.put("field_048", 7);
        expected.put("field_049", 4);
        expected.put("field_050", 4);
        expected.put("field_051", 4);
        expected.put("field_052", 5);
        expected.put("field_053", 4);
        expected.put("field_054", 4);
        expected.put("field_055", 5);

        expected.put("field_056", 5);
        expected.put("field_057", 5);
        expected.put("field_058", 4);
        expected.put("field_059", 4);
        expected.put("field_060", 4);
        expected.put("field_061", 4);
        expected.put("field_062", 4);
        expected.put("field_063", 4);
        expected.put("field_064", 4);
        expected.put("field_065", 4);
        expected.put("field_066", 3);
        expected.put("field_067", 4);
        expected.put("field_068", 4);
        expected.put("field_069", 4);
        expected.put("field_070", 3);
        expected.put("field_071", 4);
        expected.put("field_072", 5);
        expected.put("field_073", 4);
        expected.put("field_074", 3);
        expected.put("field_075", 4);
        expected.put("field_076", 4);
        expected.put("field_077", 4);
        expected.put("field_078", 4);
        expected.put("field_079", 5);
        expected.put("field_080", 5);
        expected.put("field_081", 5);
        expected.put("field_082", 5);
        expected.put("field_083", 5);
        return expected;
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> expected = getExpected();
        CounterVisitor cv = new CounterVisitor();
        Files.walkFileTree(Paths.get("extractedRegions/200DPI/"), cv);
        List<String> fields = new ArrayList<>(cv.count.keySet());
        Collections.sort(fields);
        int sum = 0;
        int all = 0;
        int under = 0;
        int over = 0;
        for (String field : fields) {
            // System.out.println(field + " expected: " + expected.get(field));
            Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
            Map<String, Integer> regionsNumber = cv.count.get(field);
            for (String region : regionsNumber.keySet()) {
                int number = regionsNumber.get(region);
                frequency.put(number, frequency.getOrDefault(number, 0) + 1);
            }
            int succ = frequency.getOrDefault(expected.get(field), 0);
            sum += succ;
            all += 22;
            System.out.println("Successful: " + succ + ", negative: " + (22 - succ) + ", rate: "
                    + Math.round(succ * 10000.0 / 22.) / 100.0);
            int oversegmented = 0, undersegmented = 0;
            for (Integer key : frequency.keySet()) {
                if (key > expected.get(field)) {
                    oversegmented += frequency.get(key);
                } else if (key < expected.get(field)) {
                    undersegmented += frequency.get(key);
                }
            }
            under += undersegmented;
            over += oversegmented;
            System.out.println("Over segmented: " + Math.round(oversegmented * 10000.0 / 22.0) / 100.0
                    + " undersegmented: " + Math.round(undersegmented * 10000.0 / 22.) / 100.0);
            System.out.println(frequency);
            // System.out.println(expected.get(field) + "\t" + Math.round(succ * 10000.0 / 22.) / 100.0 + "\t"
            // + Math.round(oversegmented * 10000.0 / 22.0) / 100.0 + "\t"
            // + Math.round(undersegmented * 10000.0 / 22.) / 100.0);
            System.out.println();

        }

        System.out.println("Success ratio: " + sum + "/" + all);
        System.out.println("Oversegment " + over + " undersegment " + under);
    }

    /**
     * Private utility demo class constructor.
     */
    private RegionsNumberDemo() {
    }
}
