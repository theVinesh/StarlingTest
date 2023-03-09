package com.example.starlingtest.di

import com.example.starlingtest.api.ApiFactory
import com.example.starlingtest.api.HeaderInterceptor
import com.example.starlingtest.api.KotlinXConvertorFactory
import com.example.starlingtest.ui.accounts.data.AccountsRepository
import com.example.starlingtest.ui.accounts.reducers.AccountsStateReducer
import com.example.starlingtest.ui.accounts.usecases.RefreshAccountsUseCase
import com.example.starlingtest.ui.goals.data.GoalsRepository
import com.example.starlingtest.ui.goals.reducers.CreateGoalDialogStateReducer
import com.example.starlingtest.ui.goals.reducers.GoalsStateReducer
import com.example.starlingtest.ui.goals.usecases.CreateGoalUseCase
import com.example.starlingtest.ui.goals.usecases.RefreshGoalsUseCase
import com.example.starlingtest.ui.goals.usecases.TransferToGoalUseCase
import com.example.starlingtest.utils.networking.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/"

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(KotlinXConvertorFactory.getConvertorFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
    ) = OkHttpClient.Builder()
        .addNetworkInterceptor(HeaderInterceptor())
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    @Provides
    fun provideApiFactory(
        retrofit: Retrofit
    ) = ApiFactory(retrofit)

    @Provides
    fun provideStarlingTestApi(
        apiFactory: ApiFactory
    ) = apiFactory.createStarlingTestApi()
}

@Module
@InstallIn(ViewModelComponent::class)
object AccountsModule {

    @Provides
    fun provideAccountsRepository(
        apiFactory: ApiFactory
    ) = AccountsRepository(apiFactory.createStarlingTestApi())

    @Provides
    fun provideRefreshAccountsUseCase(
        repository: AccountsRepository
    ) = RefreshAccountsUseCase(repository)

    @Provides
    fun provideAccountsStateReducer() = AccountsStateReducer()
}

@Module
@InstallIn(ViewModelComponent::class)
object GoalsModule {

    @Provides
    fun provideGoalsRepository(
        apiFactory: ApiFactory
    ) = GoalsRepository(apiFactory.createStarlingTestApi())

    @Provides
    fun provideRefreshGoalsUseCase(
        repository: GoalsRepository
    ) = RefreshGoalsUseCase(repository)

    @Provides
    fun provideCreateGoalUseCase(
        repository: GoalsRepository
    ) = CreateGoalUseCase(repository)

    @Provides
    fun provideTransferToGoalUseCase(
        repository: GoalsRepository
    ) = TransferToGoalUseCase(repository)

    @Provides
    fun provideGoalsStateReducer() = GoalsStateReducer()

    @Provides
    fun provideCreateGoalDialogStateReducer() = CreateGoalDialogStateReducer()
}
