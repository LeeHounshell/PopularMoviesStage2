package com.smartvariables.lee.popularmovies;

import com.smartvariables.lee.popularmovies.util.CircleFragment;

public interface PerformReviewSearchContext {
    public void setReviewsList(MovieReviewInfoList reviewList, boolean scrollOk);
    public CircleFragment getCircleFragment(); // integrate with CircleView
    public MovieReviewInfoList getDefaultReviewListForMovie(long movieRowId);
}
