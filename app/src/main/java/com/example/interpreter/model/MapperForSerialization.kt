package com.example.interpreter.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Polymorphic

data class MapperForSerialization(
    val blocks: List<Block>)