package tison.com.outerspacemanagaer.outerspacemanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atison on 23/01/2018.
 */

public class Searches {
    private String size;
    private Search[] searches;

    public void setSearches(Search[] searches) {
        this.searches = searches;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Search[] getSearches() {
        return searches;
    }

    public String getSize() {
        return size;
    }
}
