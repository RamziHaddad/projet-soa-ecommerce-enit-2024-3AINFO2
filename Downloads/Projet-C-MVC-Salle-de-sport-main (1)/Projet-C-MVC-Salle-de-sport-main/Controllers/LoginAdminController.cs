using ProjetC_MVCSalleSport.Models;
using System.Linq;
using System.Web.Mvc;

namespace ProjetC_MVCSalleSport.Controllers
{
    public class LoginAdminController : Controller
    {
        private ContexteDeBaseDeDonnees db = new ContexteDeBaseDeDonnees();

        // GET: /LoginAdmin/LoginAdmin
        public ActionResult LoginAdmin()
        {
            return View();
        }

        // POST: /LoginAdmin/LoginAdmin
        [HttpPost]
        public ActionResult LoginAdmin(Admin admin)
        {
            var adminTrouve = db.Admins.FirstOrDefault(a => a.mail == admin.mail && a.motDePasse == admin.motDePasse);

            if (adminTrouve != null)
            {
                // Redirection vers une autre vue (par exemple, la page d'accueil)
                Session["UserType"] = "admin";
                return RedirectToAction("Index", "Home");
            }
            else
            {
                ModelState.AddModelError("", "Email ou mot de passe incorrect");
                return View(admin);
            }
        }
        // GET: /LoginAdmin/Logout
        public ActionResult Logout()
        {
            // Code de déconnexion
            Session.Clear(); // Effacer toutes les variables de session
            return RedirectToAction("Index", "Home");
        }
    }
}