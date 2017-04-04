/**
 * Created by Rodica on 3/30/2017.
 */
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class math {
    public static void main(String arg[]) {
        //DescriptiveStat. salveaza datele in memorie
        DescriptiveStatistics descStat = new DescriptiveStatistics();
        //SummStat nu salveaza datele in memorie,poate fi folosit pentru siruri lungi
        SummaryStatistics summStats = new SummaryStatistics();

        int dateArray[] = {1, 2, 3, 4, 5};

        for (int i = 0; i < dateArray.length; i++) {
            descStat.addValue(dateArray[i]);
            summStats.addValue(dateArray[i]);
        }

        double media = descStat.getMean();
        double std = descStat.getStandardDeviation();

        double max = summStats.getMax();
        double suma = summStats.getSum();
        System.out.println("Affisarea rezultatului: \n");
        System.out.println("Media elementelor: " + media + "\n");
        System.out.println("Devierea standarta: " + std + "\n");
        System.out.println("Affisarea rezultatului: \n");
        System.out.println("Elimentul maxim: " + max + "\n");
        System.out.println("Devierea standarta: " + suma + "\n");
    }
}
