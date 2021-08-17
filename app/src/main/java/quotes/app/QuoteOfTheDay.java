package quotes.app;

import quotes.Quote;

import java.util.Date;

public class QuoteOfTheDay{
    public Date qotd_date;
    public QuoteAPI quote;

    public Quote toQuote(){
        Quote newQuote = new Quote();
        newQuote.text = this.quote.body;
        newQuote.author = this.quote.author;

        return newQuote;
    }
}

