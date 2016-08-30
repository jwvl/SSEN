package simulate.analysis.visualize;

import simulate.analysis.CandidateMappingTable;

/**
 * Created by janwillem on 30/08/16.
 */
public class GoogleSankey {

    public static String toHtml(CandidateMappingTable candidateMappingTable) {
        return getFirstPart()+candidateMappingTable.toJsRows()+getSecondPart();
    }

    private final static String getFirstPart() {
        return "<html>\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      google.charts.load('current', {'packages':['sankey']});\n" +
                "      google.charts.setOnLoadCallback(drawChart);\n" +
                "\n" +
                "      function drawChart() {\n" +
                "        var data = new google.visualization.DataTable();\n" +
                "        data.addColumn('string', 'From');\n" +
                "        data.addColumn('string', 'To');\n" +
                "        data.addColumn('number', 'Weight');\n" +
                "        data.addRows(";
    }

    private final static String getSecondPart() {
        return  ");\n" +
                "\n" +
                "        // Sets chart options.\n" +
                "        var options = {\n" +
                "          width: 900,\n" +
                "        };\n" +
                "\n" +
                "        // Instantiates and draws our chart, passing in some options.\n" +
                "        var chart = new google.visualization.Sankey(document.getElementById('sankey_basic'));\n" +
                "        chart.draw(data, options);\n" +
                "      }\n" +
                "    </script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"sankey_basic\" style=\"width: 900px; height: 300px;\"></div>\n" +
                "  </body>\n" +
                "</html>\n";
    }
}


