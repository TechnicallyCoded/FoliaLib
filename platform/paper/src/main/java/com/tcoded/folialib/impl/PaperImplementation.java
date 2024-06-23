package com.tcoded.folialib.impl;

import com.tcoded.folialib.FoliaLib;

@SuppressWarnings("unused")
public class PaperImplementation extends FoliaImplementation {

    public PaperImplementation(FoliaLib foliaLib) {
        super(foliaLib);
    }

    // Don't need to override anything, since we're extending FoliaImplementation
    // and all methods are the same as in folia starting from paper 1.20
}
