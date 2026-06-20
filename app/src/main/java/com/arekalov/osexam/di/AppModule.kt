package com.arekalov.osexam.di

import android.content.Context
import com.arekalov.osexam.data.AssetTicketBlocksRepository
import com.arekalov.osexam.data.AssetTicketRepository
import com.arekalov.osexam.data.TicketBlocksParser
import com.arekalov.osexam.data.TicketParser
import com.arekalov.osexam.domain.repository.TicketBlocksRepository
import com.arekalov.osexam.domain.repository.TicketRepository
import com.arekalov.osexam.domain.usecase.GetTicketBlocksUseCase
import com.arekalov.osexam.domain.usecase.GetTicketListUseCase
import com.arekalov.osexam.domain.usecase.GetTicketUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTicketParser(): TicketParser = TicketParser()

    @Provides
    @Singleton
    fun provideTicketBlocksParser(): TicketBlocksParser = TicketBlocksParser()

    @Provides
    @Singleton
    fun provideTicketRepository(
        @ApplicationContext context: Context,
        parser: TicketParser
    ): TicketRepository {
        return AssetTicketRepository(context.assets, parser)
    }

    @Provides
    @Singleton
    fun provideTicketBlocksRepository(
        @ApplicationContext context: Context,
        parser: TicketBlocksParser
    ): TicketBlocksRepository {
        return AssetTicketBlocksRepository(context.assets, parser)
    }

    @Provides
    fun provideGetTicketListUseCase(repository: TicketRepository): GetTicketListUseCase {
        return GetTicketListUseCase(repository)
    }

    @Provides
    fun provideGetTicketUseCase(repository: TicketRepository): GetTicketUseCase {
        return GetTicketUseCase(repository)
    }

    @Provides
    fun provideGetTicketBlocksUseCase(repository: TicketBlocksRepository): GetTicketBlocksUseCase {
        return GetTicketBlocksUseCase(repository)
    }
}
