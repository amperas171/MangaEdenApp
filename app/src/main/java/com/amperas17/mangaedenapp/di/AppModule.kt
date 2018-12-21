package com.amperas17.mangaedenapp.di

import com.amperas17.mangaedenapp.ui.gallery.viewmodel.ChapterPagesViewModel
import com.amperas17.mangaedenapp.ui.mangafull.viewmodel.MangaFullViewModel
import com.amperas17.mangaedenapp.ui.mangalist.viewmodel.MangaListViewModel
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module


val appModule = module {
    viewModel<MangaListViewModel>()
    viewModel<MangaFullViewModel>()
    viewModel<ChapterPagesViewModel>()
}