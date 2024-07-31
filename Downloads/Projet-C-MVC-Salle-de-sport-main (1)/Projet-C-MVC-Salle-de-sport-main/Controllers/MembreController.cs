using ProjetC_MVCSalleSport.Models;
using System.Data.Entity;
using System.Linq;
using System.Web.Mvc;


namespace ProjetC_MVCSalleSport.Controllers
{
    public class MembreController : Controller
    {
        private readonly ContexteDeBaseDeDonnees _context;

        public MembreController()
        {
            _context = new ContexteDeBaseDeDonnees();
        }

        [HttpGet]
        public ActionResult ListeMembres()
        {
            var membres = _context.Membres.ToList();
            return View("listeMembres", membres);
        }


        [HttpGet]
        public ActionResult Ajouter()
        {
            return View("AjouterMembre");
        }


        [HttpPost]
        public ActionResult AjouterMembre(Membre membre)
        {
            if (ModelState.IsValid)
            {
                _context.Membres.Add(membre);
                _context.SaveChanges();
                return RedirectToAction("ListeMembres");
            }
            return View(membre);
        }
        [HttpGet]
        public ActionResult Supprimer()
        {
            var ListeMembres = _context.Membres.ToList();
            return View("SupprimerMembre", ListeMembres);
        }


        [HttpPost]
        public ActionResult SupprimerMembre(int? idMembre)
        {
            var membreASupprimer = _context.Membres.Find(idMembre);

            if (membreASupprimer != null)
            {
                _context.Membres.Remove(membreASupprimer);
                _context.SaveChanges();
            }

            return RedirectToAction("ListeMembres");
        }
        [HttpPost]
        public ActionResult AjouterProgres(Progrés progres)
        {
            if (ModelState.IsValid)
            {
                // Ajouter le progrès au contexte de la base de données
                _context.Progres.Add(progres);
                _context.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(progres);
        }

        [HttpGet]
        public ActionResult ConsulterProgres(int id)
        {
            var membre = _context.Membres.Include(m => m.Progres).FirstOrDefault(m => m.id_membre == id);
            return View("ConsulterProgres", membre);
        }


    }
}
