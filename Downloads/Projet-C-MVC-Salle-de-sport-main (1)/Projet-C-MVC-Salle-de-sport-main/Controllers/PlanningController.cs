using ProjetC_MVCSalleSport.Models;
using System;
using System.Data.Entity.Core;
using System.Linq;
using System.Web.Mvc;

namespace ProjetC_MVCSalleSport.Controllers
{
    public class PlanningController : Controller
    {
        private readonly ContexteDeBaseDeDonnees _context;

        public PlanningController()
        {
            _context = new ContexteDeBaseDeDonnees();
        }

        [HttpGet]
        public ActionResult Index()
        {
            try
            {
                // Récupérer uniquement les champs nécessaires depuis la base de données
                var coursList = _context.Cours
                                        .Select(c => new
                                        {
                                            c.nom,
                                            c.date,
                                            c.heure,
                                            c.NomCoach // Utilisation de la propriété NomCoach
                                        })
                                        .ToList()
                                        .Select(c => new Cours
                                        {
                                            nom = c.nom,
                                            date = c.date,
                                            heure = c.heure,
                                            NomCoach = c.NomCoach
                                        })
                                        .ToList();

                // Retourner les cours à la vue
                return View(coursList);
            }
            catch (EntityCommandExecutionException ex)
            {
                // Logguer l'exception ou afficher les détails de l'exception interne
                Console.WriteLine("Une exception s'est produite lors de l'exécution de la commande : " + ex.InnerException.Message);
                throw; // Re-lancer l'exception pour une gestion ultérieure si nécessaire
            }
        }
    }
}
