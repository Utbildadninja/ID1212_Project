package kth.se.id1212.model;

public class LanguageBean {
    int languageID;
    String languageName;

    public LanguageBean(int id, String languageName) {
        this.languageID = id;
        this.languageName = languageName;
    }

    public int getLanguageID() {
        return languageID;
    }

    public String getLanguageName() {
        return languageName;
    }

}
