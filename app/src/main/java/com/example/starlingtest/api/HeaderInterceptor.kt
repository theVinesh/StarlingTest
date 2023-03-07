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
            "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31Uy27bMBD8lUDnMHD0pHTrrT_QD1gtVzZhihRIKmlQ9N9LiZRlOUYvBmZmH7Pclf9k0rmsy2CSTNBo3pwHq6Q-96Cvb2jG7DVzcx8iqlaIoe4Fw5YjK8ueM8Dw0xTvRdGeOG95GYLp95R173XDi1PbNtVrJsFHIs-LaiEA0cza_zRKkP0lxVK7Gtqccs5Op_eClVCVjCO0DPIC67rq8-JUhNreXEnHDOwrLoYeWc6Bs5LXLetzGFgLTdnUHJpqECEjjPUDkZyLWWUuGgB-Ys3AB1ZSU7AeoWdFHVqKgou6zpeB0Uy0PEp0ylAZR6KzBOJl4y6rfaZhpKeC_5oeBClIezlIskdeSecPTAJC2GC8IyH9DUTFe8DLSLfIHX9a6ekFZn8xVrqwRia1kB9SzKBicA8KNCZrCDbs02hvjYqNFiZpRg_SjuCl0cwMbJi1cDfJ3bpvILbG2XkzbiPSCDIVVhSM6HMH06S-bmiNGkEL8NQJUhRKbDBp9kp-GWSyNJCl4N39T4o2ojYpQAov4Ols1znuE7-LKZUsXmCbbiQPwQ10GOCqJrwONcEX0SZFkIaIYA9icoRzmikRobPV21pi_J3sLWgHuLsONOtnde227dJO7Q4i3k1EvBVYbiTc2yj9XlMZDCbuKqwEM8uRPLIpy5pBqm2kOOOBWqMsIcnJH4A7SvHB1-VZUEl08BF26tjZ7LYOXBruwH3LjO8XCj8rsYtPau1iLIoXErMiwdL7JZq8D_POU4ITbN9U-ANdb5sZK-7aH9mt75F9ks_Mp77xntYFovt4pCYxJGruHdrwuMvdbF3uuTXq_rjW_T1eW_b3H_5Y6xIZBgAA.nWSdu-gLNRmC6kL9TkuTh_a28DiOTnC0igw4S_a5npsVKd9MleOo6dWYKw6_pmfMGRqaZqPHxi7o2BYVLiN-yBWuBM2cFiO_f4nqjZJFzPKywT5eEnz5EMyCdKmIuu_rVnvA4oPIasVDcXrqSCtlJ84pzHLD6QJuj7vQW3-we7ov75JlWGPmvOfOXFy5CjJtxl_JmptcDJo8LkENMttOwXghdQhX7kGAHPG6ZGN3S24xdx2Hkk4pNN5o8d-QaOcxgP67Xu90MJ9sct1zAUr2-qgoI74he7TUF3Qx2MmuRroUBIVxAr82G6Kh7IhVsBm0qAF20daz3Deht9-EicDkrGDJf8rwQNcFcalzwIGfCFJaAhfU_FYoruT-2VNLaR07k0HlWNT6UOG6zEpcdyspZoUW1FEY6Uqadf5RYRABqDdGHPPVD9HeFBewzAIZSOEDubl8D78q-1LSAtC6rLjcb9JLHGKuA8Ej5AQXVE8dEDG55hCJfywQS1rKjPPnUhhn3eTDY90K0dnWA6Y05ym2vr7SMq8rwb37L8xY8rvm9ERXVW1hTny7Hjm9nmtw3W0Q1X4DJ7eck2qd2194OsCs2E995Xwgsc7IJ8pmGc8j4UTyTBAEmdx13dW4GN8CIU-aw_43j_-qMQQkKzsVkkm6fLunzLPt0zFLxVXs-3eZaFc"
    }
}
