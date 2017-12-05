package net.epictimes.uvindex

class Constants {

    class RequestCodes {
        companion object {
            val INSTALL_FROM_QUERY_FEATURE = 1
            val UPDATE_LOCATION_SETTINGS = 2
            val START_AUTOCOMPLETE = 3
        }
    }

    class ReferrerCodes {
        companion object {
            val FROM_QUERY_FEATURE = "itself"
        }
    }

    class BundleKeys {
        companion object {
            val ADDRESS = "address"
        }
    }

}