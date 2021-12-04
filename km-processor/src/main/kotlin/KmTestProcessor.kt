import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated

class KmTestProcessor : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.logging("多平台 ksp environment")
        return TSymbolProcessor(environment.codeGenerator)
    }

    class TSymbolProcessor(
        private var codeGenerator: CodeGenerator
    ) : SymbolProcessor {
        private var f = false
        override fun process(resolver: Resolver): List<KSAnnotated> {
            if (f) return emptyList()

            val packageName = "com.km.processor"
            val writer = codeGenerator.createNewFile(
                Dependencies(false),packageName,"KspTest"
            ).writer()

            writer.append("""
                package $packageName
                import kotlin.random.Random

                object KspTest {
                    fun random() = Random.nextInt()
                    const val string = "ksp"
                }
            """.trimIndent())

            writer.flush()

            f = true
            return emptyList()
        }

    }
}