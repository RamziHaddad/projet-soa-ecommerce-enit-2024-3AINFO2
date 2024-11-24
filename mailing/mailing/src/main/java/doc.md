MailResource as the single entry point for the API. It should handle:
Receiving and validating incoming requests.
Delegating to the appropriate service (e.g., MailtrapService, TemplateService).