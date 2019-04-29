package com.example.photocollage

import android.graphics.Bitmap

interface OnProcessingCompletionListener {
  fun onProcessingComplete(bitmap: Bitmap)
}