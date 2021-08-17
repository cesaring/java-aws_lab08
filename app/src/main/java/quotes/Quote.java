package quotes;
    import java.util.ArrayList;

public class Quote {
    public ArrayList<String> tags = new ArrayList<String>();
    public String author;
    public String likes;
    public String text;

    public String toString(){
        String result;
        result = text +"\n";
        result = result + "--"+ author;

        return result;

    }
}

