package com.zero.healthmonitoring.data

class UserTestBean {

    var tonum: String? = null
    var num: Int = 0
    var curpage: Int = 0
    var bloinfo: List<BloinfoBean>? = null

    class BloinfoBean {
        /**
         * id : 22
         * uid : 123
         * spo : 99
         * bpm : 88
         * minhg : null
         * addtime : 2019-11-08 16:08:57
         */

        var id: String? = null
        var uid: String? = null
        var spo: String? = null
        var bpm: String? = null
        var minhg: Any? = null
        var addtime: String? = null
    }
}
