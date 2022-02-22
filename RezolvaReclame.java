import java.io.*;

public class RezolvaReclame extends Task {
    public int m;
    public int n;
    public int k;
    boolean success;
    public int[][] matrix;
    public int[][] adiacent_matrix;

    public RezolvaReclame() { }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getAdiacent_matrix() {
        return adiacent_matrix;
    }

    public void setAdiacent_matrix(int[][] adiacent_matrix) {
        this.adiacent_matrix = adiacent_matrix;
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        success = false;
        k = 1;
        while (!success) {
            formulateOracleQuestion();
            askOracle();
            decipherOracleAnswer();
            k++;
        }
    }

    @Override
    public void readProblemData() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        String st;
        st = br.readLine();
        String[] values = st.split(" ");
        int[] iValues = new int[2];
        for (int i = 0; i < 2; i++) {
            iValues[i] = Integer.parseInt(values[i]);
        }

        n = iValues[0];
        m = iValues[1];
        int[][] newMatrix = new int[m+1][2];
        setAdiacent_matrix(new int[n + 1][n + 1]);
        int i = 1;
        while (i != m+1) {
            st = br.readLine();
            int j = 0;
            String[] valuesMatrix = st.split(" ");
            int[] iValuesMatrix = new int[2];
            for (int k = 0; k < 2; k++) {
                iValuesMatrix[k] = Integer.parseInt(valuesMatrix[k]);
            }

            newMatrix[i][j] = iValuesMatrix[0];
            newMatrix[i][j+1] = iValuesMatrix[1];
            i++;
        }

        setMatrix(newMatrix);

        for (int j = 1; j <= m; j++) {
            int x = getMatrix()[j][0];
            int y = getMatrix()[j][1];
            getAdiacent_matrix()[x][y] = 1;
            getAdiacent_matrix()[y][x] = 1;
        }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter myFileWriter = new
                FileWriter("sat.cnf");

        myFileWriter.write("p cnf");
        myFileWriter.write(" ");

        int nr_variables = n * k;
        String variables = Integer.toString(nr_variables);

        myFileWriter.write(variables);
        myFileWriter.write(" ");

        int clauses = 0;
        clauses += k;
//        clauses +=  n * (k * (k - 1) / 2);
        clauses += (n * n - n - 2 * m) * k * (k - 1) / 2;
        myFileWriter.write(String.valueOf(clauses));

        myFileWriter.write("\n");

        for (int i = 1; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                int x = (i - 1) * n;
                x += j;
                String x_ij = Integer.toString(x);
                myFileWriter.write(x_ij);
                myFileWriter.write(" ");
            }
            myFileWriter.write("0");
            myFileWriter.write("\n");
        }

        for (int u = 1; u <= n; u++) {
            for (int i = 1; i <= k - 1; i++) {
                for (int j = i + 1; j <= k; j++) {
                    int x = (i - 1) * n;
                    x += u;
                    x = -x;
                    String x_ij = Integer.toString(x);
                    myFileWriter.write(x_ij);
                    myFileWriter.write(" ");
                    int y = (j - 1) * n;
                    y += u;
                    y = -y;
                    String y_ij = Integer.toString(y);
                    myFileWriter.write(y_ij);
                    myFileWriter.write(" 0");
                    myFileWriter.write("\n");
                }
            }
        }

        for (int u = 1; u <= n; u++) {
            for (int v = 1; v <= n; v++) {
                if (getAdiacent_matrix()[u][v] == 1 && u != v) {
                    for (int i = 1; i <= k; i++) {
                        int x = (i - 1) * n;
                        x += u;
                        String x_ij = Integer.toString(x);
                        myFileWriter.write(x_ij);
                        myFileWriter.write(" ");
                        int y = (i - 1) * n;
                        y += v;
                        String y_ij = Integer.toString(y);
                        myFileWriter.write(y_ij);
                        myFileWriter.write(" ");
                    }
                    myFileWriter.write("0");
                    myFileWriter.write("\n");
                }
            }
        }

        myFileWriter.close();
    }


    @Override
    public void decipherOracleAnswer() throws IOException {
        File file = new File("sat.sol");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st = br.readLine();

        if (st.equals("True")) {
            success = true;
            st = br.readLine();
            st = br.readLine();
            String[] values = st.split(" ");
            int[] iValues = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                iValues[i] = Integer.parseInt(values[i]);
            }

            int contor = 0;
            for (int i = 0; i < iValues.length; i++) {
                if (iValues[i] > 0) {
                    contor++;
                }
            }

            System.out.println(contor);
            for (int i = 0; i < iValues.length; i++) {
                if (iValues[i] > 0) {
                    System.out.print(iValues[i] + " ");
                }
            }

        }
    }
}
