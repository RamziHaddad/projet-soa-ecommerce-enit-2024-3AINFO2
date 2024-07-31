using ProjetC_MVCSalleSport.Models;
using System.Linq;
using System.Web.Mvc;

namespace ProjetC_MVCSalleSport.Controllers
{
    public class AuthController : Controller
    {
        private ContexteDeBaseDeDonnees db = new ContexteDeBaseDeDonnees();
        // GET: /Auth/ChooseUser
        public ActionResult ChooseUser()
        {
            return View();
        }

        // GET: /Auth/Login
        public ActionResult Login()
        {
            return View();
        }

        // POST: /Auth/Login
        [HttpPost]
        public ActionResult Login(Membre membre)
        {
            var membreTrouve = db.Membres.FirstOrDefault(m => m.mail == membre.mail && m.motDePasse == membre.motDePasse);

            if (membreTrouve != null)
            {
                // Si le membre est trouvé, redirigez-le vers une autre vue ou effectuez d'autres actions
                Session["UserType"] = "membre";
                return RedirectToAction("Index", "Home"); // Redirection vers la page d'accueil par exemple
            }
            else
            {
                ModelState.AddModelError("", "Email ou mot de passe incorrect");
                return View(membre);
            }
        }

        // GET: /Auth/SignUp
        public ActionResult SignUp()
        {
            return View();
        }

        // POST: /Auth/SignUp
        [HttpPost]
        public ActionResult SignUp(Membre membre)
        {
            if (ModelState.IsValid)
            {
                var existingMember = db.Membres.FirstOrDefault(m => m.mail == membre.mail);
                if (existingMember != null)
                {
                    ModelState.AddModelError("", "Cet e-mail est déjà utilisé pour un autre compte.");
                    return View(membre);
                }

                db.Membres.Add(membre);
                db.SaveChanges();

                // Rediriger vers une page de confirmation ou une autre page appropriée
                return RedirectToAction("About", "Home"); // Redirection vers la page d'accueil par exemple
            }

            return View(membre);
        }

        // GET: /Auth/Confirmation
        public ActionResult Confirmation()
        {
            return View();
        }
        // GET: /Auth/Logout
        public ActionResult Logout()
        {
            // Code de déconnexion
            Session.Clear(); // Effacer toutes les variables de session
            return RedirectToAction("Index", "Home");
        }
    }
}