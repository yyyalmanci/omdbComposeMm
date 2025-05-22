import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yyy.omdbcomposemm.Greeting
import com.yyy.omdbcomposemm.navigation.RouteClass


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: RouteClass = RouteClass.Films()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<RouteClass.Films> {
            Greeting(
                name = RouteClass.Films().name,
                modifier = Modifier.padding()
            )
        }
        composable<RouteClass.Favorites> {
            Greeting(
                name = RouteClass.Favorites().name,
                modifier = Modifier.padding()
            )
        }

        composable<RouteClass.Settings> {
            Greeting(
                name = RouteClass.Settings().name,
                modifier = Modifier.padding()
            )
        }
    }
}