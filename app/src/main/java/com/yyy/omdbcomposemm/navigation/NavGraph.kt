import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yyy.omdbcomposemm.Greeting
import com.yyy.omdbcomposemm.navigation.RouteClass
import com.yyy.ui.screen.FavoritesListScreen
import com.yyy.ui.screen.MovieSearchScreen
import com.yyy.ui.viewmodel.MovieSearchViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    movieSearchViewModel: MovieSearchViewModel,
    startDestination: RouteClass = RouteClass.MovieSearch()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<RouteClass.MovieSearch> {
            MovieSearchScreen(movieSearchViewModel)
        }
        composable<RouteClass.FavoritesList> {
            FavoritesListScreen(
                onListClick = { listTitle ->
                    // TODO: Navigate to list detail screen
                }
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