import GP.BinaryParser;
import GP.UnaryParser;

import java.util.function.BiPredicate;

public class GPParser {

    public static double parse(String Tree, double[] x){
      String data = Tree;
        for (int i = 0; i < x.length; i++) {
            data = data.replace("x" + (1 + i), x[i] + "");
        }
        String tData = "";
        while (!tData.equals(data)) {
            tData = data;
            data = BinaryParser.Parse("if<\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a < b ? a : b);
            data = BinaryParser.Parse("if>\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> (a >= b) ? a : b);
            data = BinaryParser.Parse("ifsum\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a < b ? a + b : a);
            data = BinaryParser.Parse("add\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a + b);
            data = BinaryParser.Parse("sub\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a - b);
            data = BinaryParser.Parse("sub\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a - b);
            data = BinaryParser.Parse("mul\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a * b);
            data = BinaryParser.Parse("mod\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a % b);

            data = UnaryParser.Parse("abs\\(([\\-0-9.E]+)\\)", data, Math::abs);
            data = UnaryParser.Parse("exp\\(([\\-0-9.E]+)\\)", data, Math::exp);
            data = UnaryParser.Parse("sqrt\\(([\\-0-9.E]+)\\)", data, Math::sqrt);
        }
        return Double.parseDouble(data.trim());
    }

    public static void TestParse() {
        String data2 = "abs(add(abs(if>(if<(x3, x5), if>(x3, x2))), ifsum(if>(ifsum(x3, x3), sub(x4, x2)), ifsum(ifsum(x3, x4), ifsum(x1, x5)))))";
        String data3 = "add(sub(ifsum(sub(if>(ifsum(x2, x1), sub(ifsum(sub(ifsum(x2, x1), sub(sub(x3, add(abs(x1), if>(0.9374640134764072, x5))), ifsum(sub(x4, sub(x4, x4)), x4))), add(x4, x5)), sub(add(add(mul(x5, x4), abs(x4)), ifsum(abs(x5), ifsum(add(x4, x5), if>(x5, x4)))), ifsum(mul(ifsum(x2, x3), mul(x3, x1)), sub(add(x5, x4), add(x3, x5)))))), sub(sub(x5, add(abs(x1), if>(0.5804689572713363, x5))), ifsum(x1, sub(x4, sub(x4, mul(x5, x4)))))), add(ifsum(abs(x3), mul(x3, x2)), if>(sub(0.2319123310473059, 0.6423047232374376), mul(x4, x2)))), sub(add(add(x4, x5), ifsum(abs(x5), ifsum(add(x4, x5), if>(x5, x4)))), ifsum(mul(ifsum(x2, x3), mul(x3, x1)), sub(add(x5, x4), add(x3, 0.09106523017875467))))), add(abs(ifsum(sub(if>(ifsum(x2, x1), sub(ifsum(sub(x4, sub(sub(x3, add(abs(x1), if>(0.5014098055100729, x5))), ifsum(sub(x4, sub(x4, x4)), x4))), add(ifsum(abs(x3), abs(x5)), if>(abs(x5), mul(x3, x2)))), sub(add(add(mul(x5, x4), abs(x4)), ifsum(abs(x5), ifsum(add(x4, x5), if>(x5, x4)))), ifsum(mul(ifsum(x2, x3), mul(x3, x1)), sub(add(x5, x4), add(abs(x1), 0.4846407576534085)))))), sub(x2, ifsum(x1, x4))), if<(abs(x3), sub(x4, sub(x4, x4))))), abs(ifsum(if<(ifsum(x5, x5), add(x3, x1)), add(abs(x1), if>(0.33213098090799154, x5))))))";
        String data4 = "if>(add(ifsum(ifsum(if>(if>(mul(x5, x3), ifsum(x3, x2)), ifsum(ifsum(x3, ifsum(x1, x4)), if>(x3, x5))), if>(add(if<(x1, x3), add(x5, x1)), ifsum(if>(add(add(if>(ifsum(x2, x1), mul(x5, x2)), if>(add(x1, x5), sub(x4, x4))), if>(add(if>(x4, x2), ifsum(x2, x3)), add(ifsum(x1, x2), sub(x1, x1)))), x2), if<(x3, x4)))), if>(mul(if>(ifsum(x4, sub(x4, x5)), if<(x1, x2)), sub(add(x3, x2), if>(x3, x5))), if<(ifsum(ifsum(x3, x2), mul(x3, x4)), ifsum(if<(x1, x1), ifsum(x4, x3))))), if>(sub(if>(ifsum(if>(x1, x5), mul(x2, x3)), add(if<(x2, x3), mul(x4, x3))), mul(add(mul(x1, x4), add(x3, x1)), mul(sub(x1, x3), ifsum(x1, x5)))), add(if<(if<(add(x1, x4), if>(x5, x5)), sub(ifsum(x5, x4), if>(x2, x2))), add(if>(sub(x5, x5), add(x2, ifsum(x3, x1))), if<(ifsum(x5, x3), ifsum(x2, x2)))))), add(if>(add(add(sub(if<(x2, x4), mul(x3, x2)), if>(x2, sub(x4, x4))), if>(if<(if>(x4, x2), ifsum(x2, x3)), add(if<(x1, x2), if>(x3, x3)))), if<(if<(mul(if<(x1, x1), sub(x2, x5)), ifsum(add(x2, x5), sub(x4, x4))), if>(if>(ifsum(x5, x2), if<(x2, x2)), ifsum(sub(x3, x1), sub(x3, x5))))), mul(ifsum(add(add(add(x5, x1), if<(x2, x2)), ifsum(add(x2, x2), if<(x4, x2))), if>(ifsum(add(x5, x3), mul(x1, x1)), if>(if>(x2, x1), add(x4, x4)))), if>(add(mul(sub(sub(x4, x5), x3), if<(x1, x2)), if>(mul(x1, x3), if<(x5, x1))), sub(if<(x5, mul(x3, x5)), if<(add(x5, x5), sub(x3, x4)))))))";
        String data_depth12 = "abs(abs(ifsum(abs(if<(ifsum(abs(sub(ifsum(if<(if<(add(15.0, 12.0), if<(0.884033863579974, x2)), ifsum(ifsum(0.8659609943691936, 0.6729264222273867), if<(16.0, x4))), add(abs(abs(x2)), if<(if<(14.0, x4), abs(10.0)))), ifsum(sub(add(sub(x5, x3), sub(0.5685811585290633, 1.0)), if>(add(x5, x1), mul(x4, 0.7252253615832644))), add(mul(if>(1.0, x2), ifsum(x1, x3)), abs(add(x3, x4)))))), mul(abs(abs(sub(sub(mul(x1, x2), abs(x4)), if<(mul(x1, x3), ifsum(16.0, x4))))), if<(sub(ifsum(abs(sub(x3, x1)), abs(abs(x4))), if>(sub(if<(x2, x2), sub(13.0, x3)), if<(abs(x2), sub(0.624719761355972, x2)))), add(if<(sub(ifsum(7.0, x3), if<(x4, 0.2713822429795132)), if<(add(0.19303447555432474, x2), add(10.0, 0.5983849208688579))), if>(ifsum(ifsum(x4, x1), mul(x5, x3)), if<(if>(0.010501363265547181, x1), if>(x2, x4))))))), abs(abs(if>(ifsum(mul(if<(add(x5, x4), add(x2, x1)), if>(sub(x4, x3), sub(13.0, x3))), mul(mul(mul(x1, x3), if<(x1, x4)), mul(if>(x4, x4), sub(0.5850624507974399, x4)))), if<(if<(if>(if<(15.0, 0.1873386255286944), mul(0.5787124714327219, x2)), sub(mul(0.15537755943448572, x3), mul(0.47429604249803714, x4))), if>(ifsum(ifsum(0.8756891476433825, x4), ifsum(0.8379483207475226, x3)), sub(if>(8.0, x5), ifsum(x1, x2))))))))), if>(abs(mul(ifsum(if>(mul(sub(if<(if<(x5, x3), abs(6.0)), if<(abs(0.42032500684820884), abs(6.0))), abs(mul(if<(13.0, 8.0), ifsum(x4, 0.588147840063563)))), abs(sub(if<(add(0.3782869801278006, x4), mul(0.28480514282629754, 1.0)), if<(sub(x4, x3), abs(x2))))), mul(ifsum(mul(mul(if<(x2, x1), add(x4, 8.0)), abs(if>(0.43074841107522543, 0.06239518472305927))), if<(if>(if<(x1, x1), abs(x5)), if>(mul(x2, x1), if<(x4, 12.0)))), sub(mul(ifsum(if<(x4, 0.835219020184231), mul(x2, x2)), if<(add(x3, x2), if>(x5, x2))), mul(if<(if<(x1, x3), if>(x3, x5)), if<(sub(x5, x3), if<(x3, 5.0)))))), ifsum(add(abs(add(if<(sub(x1, 1.0), sub(11.0, x5)), ifsum(mul(x4, x5), if<(x1, x4)))), add(mul(abs(add(x1, x1)), mul(abs(9.0), abs(x2))), mul(if<(ifsum(x1, x3), if>(x5, x5)), ifsum(if>(7.0, 0.905286710057371), add(2.0, x5))))), ifsum(abs(ifsum(ifsum(ifsum(x3, x1), ifsum(x3, x4)), mul(mul(0.9261469091026401, x4), ifsum(x4, x2)))), mul(sub(add(if>(x1, x1), sub(0.3708746148042117, x3)), sub(sub(x1, x2), abs(x3))), if<(sub(mul(x3, x3), if>(x1, 12.0)), if>(ifsum(x5, x3), abs(x2)))))))), add(add(mul(ifsum(mul(if>(abs(if>(x4, x4)), if>(if<(0.30657914562856103, x1), if<(x2, x4))), if<(ifsum(add(x1, x5), ifsum(14.0, x3)), if>(add(x3, x5), abs(x4)))), add(if<(ifsum(sub(0.6867137676824554, x4), abs(x3)), ifsum(if>(x2, x1), if>(10.0, x2))), ifsum(add(add(x1, x3), abs(x1)), if>(ifsum(x4, x3), mul(0.28878581235039635, 11.0))))), abs(sub(mul(sub(if<(9.0, x5), ifsum(x2, x2)), if>(mul(x1, x5), mul(0.9254721864562814, 0.11263494695864273))), abs(if<(if>(x5, x2), ifsum(x1, x1)))))), mul(if<(add(if<(ifsum(sub(x3, 11.0), if<(x4, x5)), if>(sub(x3, 15.0), if>(4.0, x3))), add(mul(add(x1, x5), if>(x2, x4)), add(mul(x3, x1), if<(x4, x2)))), mul(if>(if>(if>(x5, 1.0), sub(x3, x3)), ifsum(if<(x1, x5), abs(0.4805278466296735))), add(if<(sub(x1, x4), sub(x1, x1)), abs(mul(0.45778551682660507, 1.0))))), mul(if>(if<(if>(ifsum(0.2723313001704135, 0.33811809748076926), mul(x3, x3)), sub(if<(x1, 4.0), mul(x3, x4))), mul(add(add(0.33027723444357704, 0.9686078349509363), if<(x5, x1)), abs(ifsum(1.0, x2)))), add(if>(mul(if>(x2, x5), ifsum(x1, x3)), sub(sub(x3, x4), mul(0.22363173265742697, 4.0))), ifsum(abs(abs(x1)), abs(sub(x5, 1.0))))))), abs(ifsum(if<(if<(sub(ifsum(add(x5, x3), if>(x3, 16.0)), mul(add(x5, x4), ifsum(x4, x3))), add(if<(if<(x4, 3.0), ifsum(x5, x2)), add(sub(x3, x4), if<(x4, x4)))), sub(mul(abs(ifsum(x5, x4)), abs(abs(14.0))), if<(ifsum(ifsum(x3, x1), if<(x5, x3)), if<(if>(x2, x4), sub(x4, x2))))), if>(if<(abs(if>(sub(3.0, x4), if>(x4, 3.0))), sub(if>(mul(0.049497682715468394, x1), mul(x4, x5)), sub(sub(0.15131644357780294, 5.0), ifsum(x5, 0.5020876780019184)))), sub(if>(if>(mul(x2, 0.6840808453942676), abs(x5)), if>(mul(x1, x1), add(x2, x5))), add(ifsum(add(x5, 0.07218211009775999), mul(x3, x2)), if<(mul(x3, x5), add(x5, x4))))))))))))";
        String data5 = "if>(add(x4, abs(x1)), ifsum(add(mul(mul(mul(x4, x1), x3), abs(if<(if<(mul(x3, x1), x3), if>(ifsum(x3, 0.931274666570315), add(mul(add(x5, 9.0), abs(x1)), 0.28051227178011817))))), sub(if>(ifsum(x3, 0.3272690082878168), add(mul(add(x5, 9.0), abs(x1)), 0.8226692000834849)), mul(ifsum(if<(add(mul(mul(mul(x4, x1), x3), abs(x1)), sub(if>(ifsum(15.0, 0.2305026728636118), add(x4, mul(add(x5, 14.0), abs(x1)))), mul(x1, x2))), ifsum(add(mul(mul(mul(7.0, x1), x3), abs(if<(if<(mul(x3, x1), x3), add(mul(x4, x1), 0.02106273270189496)))), sub(if>(ifsum(x3, 0.41749035558077907), add(mul(add(x5, 8.0), abs(x1)), 0.39677936007201464)), mul(ifsum(if<(add(mul(mul(mul(x4, x1), x3), abs(x1)), sub(if>(ifsum(4.0, 0.7998968210129981), add(x4, mul(add(x5, 12.0), abs(x1)))), mul(x1, x2))), x1), 0.6896608249364873), abs(x5)))), 0.6149485936238382)), 0.6627747665430482), abs(x5)))), 0.44208362559815517))";
        String dataa = "if>(mod(mul(mod(if<(0.6188015808874265, x4), 0.509720120679946), exp(0.023875793314220184)), add(if>(mul(0.5414360139675677, mod(if<(0.6767485546549212, x4), 0.36686053189732293)), ifsum(x4, x4)), sub(mod(sub(sqrt(exp(if>(0.689775235473311, x2))), 0.32733004817761135), mul(0.5908981125235542, exp(sqrt(if>(x4, x4))))), if>(sub(exp(sqrt(if>(x4, x4))), 0.37317966077003306), ifsum(x3, x3))))), ifsum(exp(if<(0.037250237864197566, x5)), if>(mul(mod(x4, mul(0.07532001492067153, x3)), exp(x2)), exp(mul(0.2082061781449347, x1)))))";

        String data = dataa;
        double[] x = new double[]{0.31, 0.42, 0.36, 0.34, 0.35};
        for (int i = 0; i < x.length; i++) {
            data = data.replace("x" + (1 + i), x[i] + "");
        }

        System.out.println("data = " + data);
        String tData = "";
        while (!tData.equals(data)) {
            tData = data;
            data = BinaryParser.Parse("if<\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a < b ? a : b);
            data = BinaryParser.Parse("if>\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> (a >= b) ? a : b);
            data = BinaryParser.Parse("ifsum\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a < b ? a + b : a);
            data = BinaryParser.Parse("add\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a + b);
            data = BinaryParser.Parse("sub\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a - b);
            data = BinaryParser.Parse("sub\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a - b);
            data = BinaryParser.Parse("mul\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a * b);
            data = BinaryParser.Parse("mod\\(([\\-0-9.E]+), ([\\-0-9.E]+)\\)", data, (a, b) -> a % b);

            data = UnaryParser.Parse("abs\\(([\\-0-9.E]+)\\)", data, Math::abs);
            data = UnaryParser.Parse("exp\\(([\\-0-9.E]+)\\)", data, Math::exp);
            data = UnaryParser.Parse("sqrt\\(([\\-0-9.E]+)\\)", data, Math::sqrt);
        }
        System.out.println("data = " + data);
    }



}