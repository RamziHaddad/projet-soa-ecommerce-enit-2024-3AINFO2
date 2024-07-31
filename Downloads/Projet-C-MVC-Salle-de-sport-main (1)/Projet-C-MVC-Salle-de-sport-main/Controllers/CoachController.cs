using ProjetC_MVCSalleSport.Models;
using System.Data.Entity;
using System.Linq;
using System.Web.Mvc;


namespace ProjetC_MVCSalleSport.Controllers
{
    public class CoachController : Controller
    {
        private readonly ContexteDeBaseDeDonnees _context;

        public CoachController()
        {
            _context = new ContexteDeBaseDeDonnees();
        }
        // GET: /Auth/LoginCoach
        public ActionResult LoginCoach()
        {
            return View();
        }

        // POST: /Coach/LoginCoach
        [HttpPost]
        public ActionResult LoginCoach(Coach coach)
        {
            var coachTrouve = _context.Coaches.FirstOrDefault(c => c.mail == coach.mail && c.motDePasse == coach.motDePasse);

            if (coachTrouve != null)
            {
                // Redirect to the appropriate action and controller after successful login
                Session["UserType"] = "coach";
                return RedirectToAction("Index", "Home"); // Redirection vers la page d'accueil par exemple

            }
            else
            {
                ModelState.AddModelError("", "Email ou mot de passe incorrect");
                return View(coach);
            }
        }

        [HttpGet]
        public ActionResult ListeCoaches()
        {
            var coaches = _context.Coaches.ToList();
            return View("ListeCoaches", coaches);
        }

        [HttpGet]
        public ActionResult Ajouter()
        {
            return View("AjouterCoach");
        }

        [HttpPost]
        public ActionResult AjouterCoach(Coach coach)
        {
            if (ModelState.IsValid)
            {
                _context.Coaches.Add(coach);
                _context.SaveChanges();
                return RedirectToAction("ListeCoaches");
            }
            return View(coach);
        }
<<<<<<< HEAD
=======
        [HttpGet]
        public ActionResult Supprimer()
        {
            var ListeCoaches = _context.Coaches.ToList();
            return View("SupprimerCoach", ListeCoaches);
        }


        [HttpPost]
        public ActionResult SupprimerCoach(int idCoach)
        {
            var coachASupprimer = _context.Coaches.Include(c => c.cours).FirstOrDefault(c => c.id_coach == idCoach);

            if (coachASupprimer != null)
            {
                foreach (var cours in coachASupprimer.cours.ToList())
                {
                    _context.Cours.Remove(cours);
                }

                _context.Coaches.Remove(coachASupprimer);
                _context.SaveChanges();
            }

            return RedirectToAction("ListeCoaches");
        }


>>>>>>> 9302fff822ecb79e10b2c0f6ae0ee2f4cc840620
        // GET: /Coach/Logout
        public ActionResult Logout()
        {
            // Code de déconnexion
            Session.Clear(); // Effacer toutes les variables de session
            return RedirectToAction("Index", "Home");
        }
    }
}
