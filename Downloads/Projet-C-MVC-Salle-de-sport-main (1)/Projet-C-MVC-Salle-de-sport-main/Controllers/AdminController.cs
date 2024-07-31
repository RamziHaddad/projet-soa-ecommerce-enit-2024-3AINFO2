using ProjetC_MVCSalleSport.Models;
using System.Linq;
using System.Web.Mvc;

namespace ProjetC_MVCSalleSport.Controllers
{
    public class AdminController : Controller
    {
        private readonly ContexteDeBaseDeDonnees _context;
        public AdminController()
        {
            _context = new ContexteDeBaseDeDonnees();
        }

        [HttpGet]
        public ActionResult ListeCours()
        {
            var cours = _context.Cours.ToList();
            return View("ListeCours", cours);
        }


        [HttpGet]
        public ActionResult Ajouter()
        {
            return View("AjouterCours");
        }


        [HttpPost]
        public ActionResult AjouterCours(Cours cours)
        {


            if (ModelState.IsValid)
            {
                _context.Cours.Add(cours);
                _context.SaveChanges();
                return RedirectToAction("ListeCours");
            }
            return View(cours);
        }
<<<<<<< HEAD
=======
        [HttpGet]
        public ActionResult Supprimer()
        {
            var listeCours = _context.Cours.ToList();
            return View("SupprimerCours", listeCours);
        }


        [HttpPost]
        public ActionResult SupprimerCours(int idCours)
        {
            var coursASupprimer = _context.Cours.Find(idCours);

            if (coursASupprimer != null)
            {
                _context.Cours.Remove(coursASupprimer);
                _context.SaveChanges();
            }

            return RedirectToAction("ListeCours");
        }
>>>>>>> 9302fff822ecb79e10b2c0f6ae0ee2f4cc840620
    }
}
