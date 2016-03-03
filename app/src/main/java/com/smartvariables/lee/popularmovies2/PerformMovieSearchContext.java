package com.smartvariables.lee.popularmovies2;

import com.smartvariables.lee.popularmovies2.util.CircleFragment;

public interface PerformMovieSearchContext {
    public void setMovieList(MovieInfoList movieList);
    public CircleFragment getCircleFragment(); // integrate with CircleView
    public MovieInfoList getDefaultMovieList();
}
