package projectCooking.Service;

import java.util.List;

import projectCooking.Model.RecipesDTO;

public interface FavoritesService {
	public String addFavorites(Integer Id , String token ) ; 
	public String delFavorites(Integer Id , String token ) ; 
	public List<RecipesDTO> getFavorite(String token)  ; 
}
