using System.Web;
using System.Web.Mvc;

namespace ProjetC_MVCSalleSport
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
        }
    }
}
