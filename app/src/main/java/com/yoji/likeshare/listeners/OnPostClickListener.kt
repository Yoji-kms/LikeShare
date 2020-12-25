package com.yoji.likeshare.listeners

import com.yoji.likeshare.dto.Post

interface OnPostClickListener {
    fun onClick(post: Post)
}