package com.smartvariables.lee.popularmovies2;

import com.smartvariables.lee.popularmovies2.util.CircleFragment;

public interface PerformReviewSearchContext {
    public void setReviewsList(MovieReviewInfoList reviewList, boolean scrollOk);
    public CircleFragment getCircleFragment(); // integrate with CircleView
    public MovieReviewInfoList getDefaultReviewListForMovie(long movieRowId);
}
