public class FrodoKEM {
    // Parametrat kryesorë të FrodoKEM
    private final int q = 32768; // Moduli për operacionet mod
    private final int n = 640;   // Madhësia e matricës A (n x n)
    private final int nbar = 8;  // Madhësia e matricës S (n x nbar)
    private final int mbar = 8;  // Madhësia e matricës E (mbar x n)
    private final int B = 15;    // Numri i biteve për kodifikim në matrica

    // Konstruktori i klasës FrodoKEM
    public FrodoKEM() {
        // Parametrat janë të inicializuar gjatë krijimit të instancës dhe nuk kanë nevojë për ndryshime
    }

    // Getter për modulin q
    public int getQ() {
        return q;
    }

    // Getter për dimensionin n
    public int getN() {
        return n;
    }

    // Getter për dimensionin nbar
    public int getNbar() {
        return nbar;
    }

    // Getter për dimensionin mbar
    public int getMbar() {
        return mbar;
    }
}