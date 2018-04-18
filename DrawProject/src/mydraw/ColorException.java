package mydraw;

public class ColorException extends Exception {

    private static final long serialVersionUID = -3514838626508318963L;
    private String errorMessage = "An error occured. Unable to resolve given color to set.";

    ColorException() {
    }

    ColorException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
