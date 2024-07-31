using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(ProjetC_MVCSalleSport.Startup))]
namespace ProjetC_MVCSalleSport
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
