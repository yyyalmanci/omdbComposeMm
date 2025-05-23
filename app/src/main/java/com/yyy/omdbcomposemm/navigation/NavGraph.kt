import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yyy.theme.ThemeOption
import com.yyy.ui.route.RouteClass
import com.yyy.ui.screen.FavoriteListDetailScreen
import com.yyy.ui.screen.FavoritesListScreen
import com.yyy.ui.screen.MovieSearchScreen
import com.yyy.ui.screen.SettingsScreen
import com.yyy.ui.viewmodel.MovieSearchViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    movieSearchViewModel: MovieSearchViewModel,
    startDestination: RouteClass = RouteClass.MovieSearch(),
    themeOption: ThemeOption
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<RouteClass.MovieSearch> {
            MovieSearchScreen(movieSearchViewModel)
        }
        composable<RouteClass.Settings> {
            SettingsScreen(themeOption)
        }
        composable<RouteClass.FavoritesListDetail> {
            FavoriteListDetailScreen {
                navController.popBackStack()
            }
        }
        composable<RouteClass.FavoritesList> {
            FavoritesListScreen(
                onListClick = { listTitle ->
                    navController.navigate(RouteClass.FavoritesListDetail(listTitle = listTitle))
                }
            )
        }
    }
}