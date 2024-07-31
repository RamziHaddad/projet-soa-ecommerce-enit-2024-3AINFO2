using ProjetC_MVCSalleSport.Models;
using System.Linq;
using System.Web.Mvc;

namespace ProjetC_MVCSalleSport.Controllers
{
    public class CoursController : Controller
    {
        private ContexteDeBaseDeDonnees db = new ContexteDeBaseDeDonnees();

        // GET: Cours
        public ActionResult Index()
        {
            var cours = db.Cours.Include("Coaches").ToList();
            return View(cours);
        }
    }
}
