// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require("prism-react-renderer/themes/github");
const darkCodeTheme = require("prism-react-renderer/themes/dracula");

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: "L10nMessages",
  tagline: "Java Internationalization made easy!",
  url: "https://pinterest.github.io/",
  baseUrl: "/l10nmessages/",
  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",
  favicon: "img/icon.png",
  organizationName: "pinterest",
  projectName: "l10nmessages",

  presets: [
    [
      "@docusaurus/preset-classic",
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          sidebarPath: require.resolve("./sidebars.js"),
          editUrl: "https://github.com/pinterest/l10nmessages/edit/main/docs",
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      navbar: {
        title: "L10nMessages",
        logo: {
          alt: "L10nMessages Logo",
          src: "img/icon.png",
        },
        items: [
          {
            to: "/docs/introduction",
            label: "Documentation",
            position: "left",
          },
          { to: "/docs/installation", label: "Installation", position: "left" },
          {
            to: "/docs/getting-started",
            label: "Getting Started",
            position: "left",
          },
          {
            href: "https://github.com/pinterest/l10nmessages",
            label: "GitHub",
            position: "right",
          },
        ],
      },
      footer: {
        style: "dark",
        copyright: `Copyright © ${new Date().getFullYear()} Pinterest`,
      },
      prism: {
        theme: lightCodeTheme,
        darkTheme: darkCodeTheme,
        additionalLanguages: ["java", "properties"],
      },
      themeConfig: {
        metadata: [{name: 'keywords', content: 'localization, internationalization, java, l10n, i18n, icu4j'}],
      }
    }),
};

module.exports = config;
