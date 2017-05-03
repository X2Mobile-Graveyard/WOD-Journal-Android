package com.x2mobile.wodjar.util

import com.x2mobile.wodjar.data.model.UnitType

object MathUtil {

    private val KG_TO_LBS = 2.20462f

    fun convertWeight(weight: Float, weightUnit: UnitType, resultUnitType: UnitType): Float {
        if (weightUnit === resultUnitType) {
            return weight
        } else if (weightUnit === UnitType.METRIC && resultUnitType === UnitType.IMPERIAL) {
            return weight * KG_TO_LBS
        } else if (weightUnit === UnitType.IMPERIAL && resultUnitType === UnitType.METRIC) {
            return weight / KG_TO_LBS
        }
        return weight
    }

}
