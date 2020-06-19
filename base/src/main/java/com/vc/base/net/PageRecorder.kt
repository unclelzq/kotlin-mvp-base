package com.vc.base.net

class PageRecorder {
    private val firstPage = 0
    private var currentPage = firstPage
    var maxPager = firstPage

    @Synchronized
    fun getFirstPage(): Int {
        currentPage = firstPage
        return currentPage
    }

    @Synchronized
    fun toNextPage(): Int {
        if (currentPage < maxPager)
            currentPage += 1
        return currentPage
    }

    @Synchronized
    fun canLoadMore() = currentPage < maxPager
}