package com.example.starlingtest.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .header(ACCEPT, CONTENT_TYPE)
            .header(AUTHORIZATON, "$BEARER $TOKEN")

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    private companion object {
        const val AUTHORIZATON = "Authorization"
        const val ACCEPT = "Accept"
        const val BEARER = "Bearer"
        const val CONTENT_TYPE = "application/json"
        const val TOKEN =
            "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31Uy47jIBD8lZHPwyg2ft_2tj-wH9CGJkHBYAHO7Gi1_77Y4DjORHuJVFX9qKbb-ZNJ57I-g0kSjqP5cB6skvo8gL5-MDNm75mbhxBRdZyLeuCEdS0jZTm0BFj4aWhOaXdq264tQzD-nrI-r5u2yPOGNu-ZBB-JvKjoQgBjZtb-p1Ec7S_Jl9qV6AosWnI65ZSUUJWkZdARKCir62oo6ImG2t5cUccMVgrK6WkgOWtDRgcV6eoaCatETUXDi6bKQ0YY6wdj6FzMKgveALQn0ohWkBIbSgYGA6F1aMlpy-u6WAZmZsLlUaJTwpRxyHuLwN827rLaJxpGfCn4r-lJkBy1l0KiPfJKOn9gEuDcBuM9cunvICreA7uMeI_c8aeVHt9g9hdjpQtrJFJzeZN8BhWDB1CgWbLGwIZ9Gu2tUbHRwiTNaCHtCF4aTYwgYtbc3SV3776B2JrNzptxGxFHkKmwwmBEn3uYJvV1R2vUCJqDx56jwlBig0mzV_TLIJNFgRaDd_c_KdqI2qSAYXgBj2e7zvGY-F1MqWjZBbbpRvQQ3EDPAlzVhNehJvhC3KQI0hAR7EFEjnBOMyUidLZ6W0uMf5C9Be2A7a4DTYZZXfttu7hTu4OIdxMRbwWWGwn3Nkq_11SGBRMPFVaCmOVIntmUZY2Qahspznig1iiLDOXkD8Adpfjg6_IsqCQ6uIWdOnI2u60Dl4Y7cN8y4_uFwq9K7OKLWrsYi7IL8lkhJ-n9Eo3eh3nnKcEJtm8q_IGut02M5Q_tj-zW98i-yCfmU995j-sCmbs9UxMXiZoHx2x43OVuti6P3Br1eFzr_p6vLfv7D_qSg4YZBgAA.Mt4vkiBlZ4Usw3GEE4UF-k9RGL5ByePQFENd5cIy1jPfER-2zvQjSPL46QDhlnvOtz6UqwGsxH07cMpig3VSYshw-CBjwK0U9XG2b8VNNPPWKi84vV_ZtLZjU7RKUGSU5jT0OeWlNeslmt_OLL9i8f-cDI02royv5SMhjzDQsIYJUdY3VXr8030sCRE8wVXanfEntjMEvTZf1KvSHrKk7y3PlgfLv1MelWgu5LQdsvWvDqgA4lEj9jxGdnc85wsCPmI6r2Gqx45dNNneejpA9_Vs1GYWgE4WQYYBCVNhSjItvM5zchxR1sS-iBe-8KPQWkH7h5FjuU8EwKMapbBNXH6h26he9eAKp4-8LMCBg3Z4bCyjrc5KMmb736l9FZKVK8FTqWLafwz1g3OlIpsbd7nYQdxfg6uskdiIJLIcA_bpBBF9iHqKADakqDkKAAF_d037bEO_eO0vGYhaEb1Mwy-uXuq_1WRVTUcBmZsut0GnciKlQSgsP6xWAbL4N8vljODRvTmZPmyk_faue35e6OA-A6Tc1KwwJS2tReYaYd_rRR1jq8JktYMz6V1DPLD4jWxtnNkvewoo0j6bDfjPiwD2H4guk5rfNMx8Vqo2aSy7lrbnsLEa5A1RO8OOgNQ4cY2AXLF8WjBhtPo6hn7ZSv_WE3N3cF7mtns_68mL7Rc"
    }
}
