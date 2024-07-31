using System.Web.Mvc;
using System.Web.Routing;

namespace ProjetC_MVCSalleSport
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "Home", action = "Index", id = UrlParameter.Optional }
            );
            routes.MapRoute(
                name: "AjouterMembre",
                url: "Membre/Ajouter",
                defaults: new { controller = "Membre", action = "Ajouter" }
            );
            routes.MapRoute(
                name: "ListeMembres",
                url: "Membre/Liste",
                defaults: new { controller = "Membre", action = "Liste" }
            );


            routes.MapRoute(
                name: "AjouterCoach",
                url: "Coach/Ajouter",
                defaults: new { controller = "Coach", action = "Ajouter" }
            );
            routes.MapRoute(
            name: "ListeCoaches",
            url: "Coach/Liste",
            defaults: new { controller = "Coach", action = "Liste" }
            );
            routes.MapRoute(
                name: "ChooseUser",
                url: "Auth/ChooseRole",
                defaults: new { controller = "Auth", action = "ChooseUser" }
            );
            routes.MapRoute(
                name: "Login",
                url: "Auth/Login",
                defaults: new { controller = "Auth", action = "Login" }
            );

            routes.MapRoute(
                name: "LoginCoach",
                url: "Coach/LoginCoach",
                defaults: new { controller = "Auth", action = "LoginCoach" }
            );

            routes.MapRoute(
                name: "LoginAdmin",
                url: "Auth/LoginAdmin",
                defaults: new { controller = "Auth", action = "LoginAdmin" }
            );

        }
    }
}
