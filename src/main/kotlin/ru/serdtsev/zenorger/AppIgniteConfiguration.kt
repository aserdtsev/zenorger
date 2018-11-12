package ru.serdtsev.zenorger

import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.cache.CacheAtomicityMode
import org.apache.ignite.cache.CacheWriteSynchronizationMode
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.DataRegionConfiguration
import org.apache.ignite.configuration.DataStorageConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.serdtsev.zenorger.common.Realm
import ru.serdtsev.zenorger.user.User
import java.util.*

@Configuration
class AppIgniteConfiguration {

    @Bean("ignite")
    fun igniteInstance(): Ignite {

        val dataRegionCfg = DataRegionConfiguration().apply { isPersistenceEnabled = true }
        val dataStorageCfg = DataStorageConfiguration().apply {
            defaultDataRegionConfiguration = dataRegionCfg

//            val appDir = System.getProperty("user.dir")
//            storagePath = "$appDir/data/store"
//            walArchivePath = "$appDir/data/walArchive"
//            walPath = "$appDir/data/wal"
        }
        val igniteCfg = IgniteConfiguration().apply {
            isClientMode = true
            dataStorageConfiguration = dataStorageCfg
            discoverySpi = TcpDiscoverySpi().apply {
                this.ipFinder = TcpDiscoveryVmIpFinder().apply { this.setAddresses(listOf("127.0.0.1")) }
            }
        }

        val ignite = Ignition.start(igniteCfg)
        ignite.cluster().active(true)

        initCaches(ignite)

        return ignite
    }

    private fun initCaches(ignite: Ignite) {
        val userCacheCfg = getCacheCfg<User>("user", User::class.java)
        val realmCacheCfg = getCacheCfg<Realm>("realm", Realm::class.java)
        ignite.getOrCreateCaches(listOf(userCacheCfg, realmCacheCfg))
    }

    private fun <T> getCacheCfg(name: String, type: Class<*>): CacheConfiguration<UUID, T> {
        return CacheConfiguration<UUID, T>(name).apply {
            sqlSchema = "PUBLIC"
            atomicityMode = CacheAtomicityMode.TRANSACTIONAL
            backups = 1
            writeSynchronizationMode = CacheWriteSynchronizationMode.FULL_SYNC
            setIndexedTypes(UUID::class.java, type)
        }
    }
}