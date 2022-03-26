package io.github.yamin8000.cafe.ui

interface Crud<T> {

    var deleteCandidates: MutableList<T>

    fun updateCallback(item: T)

    fun deleteCallback(item: T, isChecked: Boolean)
}