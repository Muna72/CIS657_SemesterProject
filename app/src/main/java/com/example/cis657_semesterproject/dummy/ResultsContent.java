package com.example.cis657_semesterproject.dummy;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ResultsContent {

    public static final List<ResultsItem> ITEMS = new ArrayList<ResultsItem>();

    private static void addItem(ResultsItem item) {
        ITEMS.add(item);
    }

    private static ResultsItem createResultsItem(String name, ImageView currentImage) {
        return new ResultsItem(name, currentImage);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ResultsItem {
        public final String breedName;
        public final ImageView breedImage;

        public ResultsItem(String breedName, ImageView breedImage) {
            this.breedImage = breedImage;
            this.breedName = breedName;
        }

        @Override
        public String toString() {
            return "Results Item For " + this.breedName;
        }
    }
}
