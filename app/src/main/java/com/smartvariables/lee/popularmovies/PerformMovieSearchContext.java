package com.smartvariables.lee.popularmovies;

import com.smartvariables.lee.popularmovies.util.CircleFragment;

public interface PerformMovieSearchContext {
    public void setMovieList(MovieInfoList movieList);
    public CircleFragment getCircleFragment(); // integrate with CircleView
    public MovieInfoList getDefaultMovieList();
}
