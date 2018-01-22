package net.epictimes.uvindex

class Constants {

    class RequestCodes {
        companion object {
            const val INSTALL_FROM_QUERY_FEATURE = 1
            const val UPDATE_LOCATION_SETTINGS = 2
            const val START_AUTOCOMPLETE = 3
        }
    }

    class ReferrerCodes {
        companion object {
            const val FROM_QUERY_FEATURE = "itself"
        }
    }

    class BundleKeys {
        companion object {
            const val ADDRESS = "address"
        }
    }

}