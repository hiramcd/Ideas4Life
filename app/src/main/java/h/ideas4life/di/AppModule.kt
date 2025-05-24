package h.ideas4life.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import h.ideas4life.data.remote.services.IdeaService
import h.ideas4life.data.remote.services.IdeaServiceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindIdeaService(
        impl: IdeaServiceImpl
    ): IdeaService
}